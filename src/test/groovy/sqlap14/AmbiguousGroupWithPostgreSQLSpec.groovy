package sqlap14

import groovy.sql.Sql
import org.junit.Rule
import org.junit.rules.TestName
import org.yaml.snakeyaml.Yaml
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Date as SQLDate
import java.sql.SQLException

class AmbiguousGroupWithPostgreSQLSpec extends Specification {

    @Rule
    TestName testName = new TestName()

    @Shared
    static Sql DB

    def setupSpec() {
        // 初期化
        def config = new Yaml().load(getClass().getResourceAsStream('config.yml'))
        DB = Sql.newInstance(config)
        DB.execute(getClass().getResourceAsStream("init.sql").text)
        // PostgreSQL 8.4より前の場合
//        DB.execute(getClass().getResourceAsStream("create_aggregate.sql").text)

        // 日付変換用
        String.metaClass.toSQLDate = { ->
            return new SQLDate(Date.parse('yyyy-MM-dd', delegate.toString()).time)
        }

    }

    def cleanupSpec() {
        // 集約関数の削除
//        DB.execute(getClass().getResourceAsStream("drop_aggregate.sql").text)
        DB.close()
    }

    Closure selectBy = { String sqlFileName ->
        def rows = DB.rows(owner.class.getResourceAsStream("$sqlFileName").text)
        // 確認用
//        rows.each { println "${it.dump()}" }
        // 順番に依存しないようにするため
        rows.toSet()
    }


    def "14.2 アンチパターン：非グループ化列を参照する (PostgreSQLで例外が発行されないパターン)"() {

        when:
        def actual = selectBy '14_2_group_by_product_01.sql'

        then:
        actual == expect

        where:
        expect << [
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate()],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate()],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate()],
                ].toSet()
        ]

    }


    def "14.2 アンチパターン：非グループ化列を参照する (PostgreSQLで例外が発行されるパターン)"() {
        when:
        selectBy sql

        then:
        thrown(SQLException)

        where:
        sql << [
                '14_2_group_by_product_02.sql',
                '14_2_max_and_min.sql',
                '14_2_sum_by_product.sql'
        ]

    }


    def "14.4 アンチパターンを用いても良い場合(MySQL, SQLite)"() {
        when:
        selectBy '14_4_functional.sql'

        then:
        // PostgreSQLでは、例外が発行されるため
        thrown(SQLException)
    }


    def "14.5.2 相関サブクエリを使用する"() {
        when:
        def actual = selectBy '14_5_2_not_exists.sql'

        then:
        actual == expect

        where:
        expect << [
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate(), bug_id: 2],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), bug_id: 3],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), bug_id: 5],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate(), bug_id: 6],
                ].toSet()
        ]
    }


    def "14.5.3 導出テーブルを使用する"() {
        when:
        def actual = selectBy sql

        then:
        actual == expect

        where:
        sql << [
                '14_5_3_derived_table.sql',
                '14_5_3_derived_table_no_duplicates.sql'
        ]

        expect << [
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate(), bug_id: 2],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), bug_id: 3],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), bug_id: 5],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate(), bug_id: 6],
                ].toSet(),
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate(), latest_bug_id: 2],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), latest_bug_id: 5],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate(), latest_bug_id: 6],
                ].toSet()
        ]
    }


    def "14.5.4 JOINを使用する"() {
        when:
        def actual = selectBy '14_5_4_outer_join.sql'

        then:
        actual == expect

        where:
        expect << [
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate(), bug_id: 2],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), bug_id: 5],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate(), bug_id: 6]
                ].toSet()
        ]
    }


    def "14.5.5 他の列に対しても集約関数を使用する"() {
        when:
        def actual = selectBy '14_5_5_extra_aggregate.sql'

        then:
        actual == expect

        where:
        expect << [
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate(), latest_bug_id: 2],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), latest_bug_id: 5],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate(), latest_bug_id: 7],
                ].toSet()
        ]
    }


    def "14.5.6 グループ毎に全ての値を連結(MySQL, SQLite)"() {
        when:
        selectBy '14_5_6_group_concat_mysql.sql'

        then:
        // PostgreSQLではgroup_concatが無い為
        thrown(SQLException)
    }


    def "14.5.6 グループ毎に全ての値を連結(PostgreSQL)"() {
        when:
        def actual = selectBy '14_5_6_group_concat_postgres.sql'

        then:
        actual == expect

        where:
        expect << [
                [
                        [product_id: 1, latest: '2014-06-01'.toSQLDate(), bug_id_list: '1,2'],
                        [product_id: 2, latest: '2014-02-16'.toSQLDate(), bug_id_list: '3,4,5'],
                        [product_id: 3, latest: '2014-01-01'.toSQLDate(), bug_id_list: '6,7'],
                ].toSet()
        ]
    }

}
