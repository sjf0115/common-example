<#macro test>
    这是一段文本！
    <#nested>
    <#nested>
</#macro>
用法1：不调用占位符指令
<@test></@test>
用法2：调用占位符指令
<@test>这是文本后面的内容！</@test>


用法2：复杂用法
<#macro repeat count>
    <#list 1..count as x>
        <#nested x, x/2, x==count>
    </#list>
</#macro>
<@repeat count=4 ; c, halfc, last>
    ${c}. ${halfc}<#if last> Last!</#if>
</@repeat>