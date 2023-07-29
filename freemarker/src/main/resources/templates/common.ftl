<#assign common_name="Tom">

<#macro cfb num>
    <#list 1..num as i>
        <#list 1..i as j>${j}*${i}=${j*i}<#sep> </#sep></#list>
    </#list>
</#macro>