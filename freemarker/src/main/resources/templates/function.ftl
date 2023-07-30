<#function add x y>
    计算 x + y
    <#return x + y>
</#function>
用法1：调用方法变量
<#assign x=10 y=20>
${x} + ${y} = ${add(10, 20)}

用法2：验证无return指令
<#function avg x y>
<#--    <#return (x + y)/2>-->
</#function>
<#assign x=10 y=20>
(${x} + ${y})/2 = <#if avg(10, 20)?? >${avg(10, 20)}</#if>