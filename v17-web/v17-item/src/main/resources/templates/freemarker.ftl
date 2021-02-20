<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello,${username}
    <hr>
    存对象数据(时间类型特殊--?date-time-datetime)
    ID:${student.id}
    name:${student.name}
    createTime:${student.createTime?date}
    createTime:${student.createTime?time}
    createTime:${student.createTime?datetime}
    <hr>
    msg:${msg!}
    msg:${msg!"空值处理"}
    <#if msg??>
        when-present
        <#else >
        when-missing

    </#if>
    <hr>
    <h3>展示集合</h3>
    <#list list as student>
        ID:${student.id}
        name:${student.name}
        createTime:${student.createTime?datetime}
        <br>
    </#list>
    <hr>
    <h3>逻辑判断if</h3>
    <#if (money>20000)>
        还可以
        <#elseif (money>=10000)>
        一般般
        <#else >
        小伙子还需努力

    </#if>
</body>
</html>