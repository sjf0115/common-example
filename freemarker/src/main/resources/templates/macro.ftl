
<#-- 测试宏定义的位置和调用的位置 -->
<@test/>

<#-- 调用宏 sayHello -->
用法1：宏
<#macro sayHello>
    Hello World!
</#macro>
<@sayHello></@sayHello>

用法2：有参数的宏
<#macro sayHi name>
    Hi ${name}
</#macro>
<@sayHi name="Lucy"></@sayHi>
<#assign Lucy="lucy" >
<@sayHi name=Lucy></@sayHi>


用法3：有参数和默认值参数的宏
<#macro sayGoodBye n1 n2="Lucy">
    goodbye ${n1}, ${n2}
</#macro>
<@sayGoodBye n1="Tom"/>
<@sayGoodBye n1="Tom" n2="Jack"/>

用法4：支持多个参数和命名参数的宏
<#macro sayBye name extra...>
    goodbye ${name}
    <#list extra?keys as attr>
        ${attr}=${extra[attr]}
    </#list>
</#macro>
<@sayBye name="Lucy" email="1212@qq.com" phone="212121"/>


<#macro test>
    this is a macro
</#macro>