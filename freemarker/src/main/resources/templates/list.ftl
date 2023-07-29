用法1： 遍历 List
<#assign users = ["张三","李四","王五"]>
<#list users as user>
    ${user}
<#else>
    no user
</#list>

用法2： 遍历 List 的 else 分支
<#assign users = []>
<#list users as user>
    ${user}
<#else>
    no user
</#list>


<#-- FreeMarker 2.3.23 版本 遍历方式-->
用法3： 遍历 List
<#assign users = ["张三","李四","王五"]>
<#list users>
    IT 部门所有的同事：
    <#items as user>
        ${user}
    </#items>
    OK 就这样。
<#else>
    no user
</#list>

用法4： 遍历 List 的 else 分支
<#assign users = []>
<#list users>
    IT 部门所有的同事：
    <#items as user>
        ${user}
    </#items>
    OK 就这样。
<#else>
    no user
</#list>


用法5： sep 指令
<#assign users = ["张三","李四","王五"]>
<#list users>
    IT 部门所有的同事：<#items as user>${user}<#sep>,</#items>
<#else>
    no user
</#list>

IT 部门所有的同事：<#list users as user >${user}<#sep>,</#list>



<#--判断数据不为空，再执行遍历 （如果序列不存在，直接遍历会报错）-->
<#if users2??>
    <#list users2 as user>
        ${user}
    </#list>
</#if>

<#-- 当序列没有数据项时，使用默认信息 -->
<#assign users3 = []>
<#list users3 as user>
    ${user} |
<#else >
    用户数据不存在！
</#list>