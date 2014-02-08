# SQLアンチパターン読書会 14章 アンビギュアスグループ（曖昧なグループ）

## スライド

[Ambiguous Groups #sqlap](https://speakerdeck.com/grimrose/ambiguous-groups-number-sqlap)


## サンプルについて

* このプロジェクトは、[SQLアンチパターン](http://www.oreilly.co.jp/books/9784873115894/) 14章 アンビギュアスグループ（曖昧なグループ）の内容を、
PostgreSQL9.xで動かすサンプルです。


### 使い方

* **build.gradle** の以下の箇所をPostgreSQLの接続情報へ変更してください。

```
ext {
    // 適宜変更してください
    dbUrl = "jdbc:postgresql://localhost:5432/hoge"
    dbUser = "fuga"
    dbPass = "piyo"
}
```

* 以下のコマンドを実行するとテスト用の接続情報が作られます

`` $ ./gradlew createConfig ``


###ライセンス

Copyright &copy; 2014 grimrose  

Distributed under the [MIT License][mit].  

[MIT]: http://www.opensource.org/licenses/mit-license.php
