
<#assign user_name="Tom">
<#assign name="Tom" id="1" age="21">
用法1：我的名字叫 ${user_name}
用法2：我的名字叫 ${name}, 学号为 ${id}, 年龄 ${age} 岁

<#assign fruits = ["苹果", "香蕉", "葡萄"]>
用法3：我喜欢吃的水果有：
<#list fruits as fruits>
    ${fruits}
</#list>

<#assign book={"id":"1","name":"深入理解 Apache FreeMaker"} >
用法4：我正在读的书的 ID 为 ${book.id}, 书名为 ${book.name}


<#import "common.ftl" as common_namespace>
<#assign common_name="Lucy" in common_namespace>
用法5：我的名字为 ${common_name}