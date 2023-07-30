<#assign mail="tom@qq.com">
<#import "common.ftl" as cm >
乘法表：
<@cm.cfb num=8/>
邮箱：${cm.mail}
邮箱：${mail}

用法2：替换命名空间下的变量
${cm.mail}
<#assign mail="lily@qq.com" in cm>
${cm.mail}

用法3：多次 import
<#import "common.ftl" as cm1 >
<#assign mail="lily@qq.com" in cm1>
第一次 import: ${cm1.mail}
<#import "common.ftl" as cm2 >
第二次 import: ${cm2.mail}
<#import "common.ftl" as cm3 >
第三次 import: ${cm3.mail}
<#import "common.ftl" as cm4 >
第四次 import: ${cm4.mail}