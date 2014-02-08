select
        product_id
      , max(data_reported) as latest
--    PostgreSQL 8.4 より前の場合
--      , ARRAY_TO_STRING(GROUP_ARRAY(bug_id), ',') as bug_id_list
      , ARRAY_TO_STRING(ARRAY_AGG(bug_id), ',') as bug_id_list
from Bugs inner join BugsProducts using (bug_id)
group by product_id
