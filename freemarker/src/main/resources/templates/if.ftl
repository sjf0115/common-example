<#assign score = 92>
<#if score lt 60 >
    考了 ${score}, 你要加把劲了呀
<#elseif score lt 70>
    考了 ${score}, 还是很悬呀
<#elseif score lt 90 >
    考了 ${score}, 继续加油
<#else >
    考了 ${score}, 很不错！
</#if>


<#-- 只有 if 没有 elseif 和 else -->
<#assign score = 70>
<#if score gt 60>
    考了 ${score}, 继续加油！
</#if>

<#-- 只有 if 没有 elseif 但是有 else -->
<#assign score = 80>
<#if score lt 60>
    考了 ${score}, 你要加把劲了呀!
<#else>
    考了 ${score}, 继续加油！
</#if>

<#-- 嵌套 if 指令 -->
<#assign score = 80>
<#if score lt 60>
    考了 ${score}, 你要加把劲了呀!
<#else>
    <#if score gt 90 >
        考了 ${score}, 很不错！
    <#else >
        考了 ${score}, 继续加油!
    </#if>
</#if>

<#-- 判断数据是否存在 -->
<#assign list="">
<#if list??>
    数据存在
<#else >
    数据不存在
</#if>

<#assign list="a">
<#if list2??>
    数据存在
<#else >
    数据不存在
</#if>
