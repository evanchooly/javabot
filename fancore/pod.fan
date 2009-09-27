@podDepends = [Depend("sys 1.0"), Depend("inet 1.0"), Depend("sql 1.0")]
@podSrcDirs = [`fan/`]
@podIndexFacets = [@Table, @Column, @Collection]
pod fancore
{
    Str? Table := null
    Obj? Column := null
    Obj? Collection := null
}