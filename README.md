# <font style="color:#000000;">一、项目概述</font>
<font style="color:#000000;">在传统的数据分析平台中，如果我们想要分析近一年网站的用户增长趋势，通常需要手动导入数据、选择要分析的字段和图表，并由专业的数据分析师完成分析，最后得出结论。然而，本次设计的项目与传统平台有所不同。在这个项目中，用户只需输入想要分析的目标，并上传原始数据，系统将利用 A1自动生成可视化图表和学习的分析结论。这样，即使是对数据分析一窍不通的人也能轻松使用该系统。</font>

<font style="color:#000000;"></font>

# <font style="color:#000000;">二、项目介绍</font>
Q 传统 BI 平台需要按照以下步骤[查看传统 BI 平台示例]:

1.手动上传数据

2.手动选择分析所需的数据行和列(由数据分析师完成

3.需要手动选择所需的图表类型(由数据分析师完成)

4.生成图表并保存配置

本次项目所设想的智能 BI 平台

与传统的 B不同，我们的解决方案允许用户(数据分析者)仅需导入最最最原始的数据集并输入分析目标(例如网站增长趋势)，即可利用 AI 自动生成符合要求的图表和结论，从而显著提升分析效率。



# 三、需求分析
:::warning
+ 智能分析:用户输入目标和原始数据(图标类型)，可以自动生成图标和分析结论
+ 图表管理(增删改查)
+ 图表生成的异步化(消息队列)
+ 对接 Al 能力

:::





# 四、架构设计
基础流程:客户端输入分析诉求和原始数据，向业务后端发送请求。业务后端利用 A服务处理客户端数据，保持到数据库，并生成图表。处理后的数据由业务后端发送给 A服务，A服务生成结果并返回给后端，最终将结果返可给客户端展示。

ps.要根据用户的输入生成图表，所以要借用 AI 服务。

![](https://cdn.nlark.com/yuque/0/2024/png/44351073/1723018981361-976b22ac-dffa-4dc2-bdd3-3fabc499fe29.png)



上图的流程会出现一个问题:

假设一个 A1 服务生成图表和分析结果要等 50 秒，如果有大量用户需要生成图表，每个人都需要等待 50 秒，那么AI服务可能无法承受这种压力。为了解决这个问题，可以采用消息队列技术。

这类似于在餐厅点餐时，为了避免顾客排队等待，餐厅会给顾客一个取餐号码，让顾客可以先去坐下或做其他事情，等到餐厅叫到他们的号码时再去领取餐点，这样就能节省等待时间。

同样地，通过消息队列，用户可以提交生成图表的请求，这些请求会进入队列，AI服务会依次处理队列中的请求从而避免了同时处理大量请求造成的压力，同时也能更好地控制资源的使用。

:::warning
优化流程(异步化):客户端输入分析诉求和原始数据，向业务后端发送请求。业务后端将请求事件放入消息队列，并为客户端生成取餐号，让要生成图表的客户端去排队，消息队列根据 A1服务负载情况，定期检查进度，如果AI服务还能处理更多的图表生成请求，就向任务处理模块发送消息，

任务处理模块调用 A服务处理客户端数据，A服务异步生成结果返回给后端并保存到数据库，当后端的 A服务生成完毕后，可以通过向前端发送通知的方式，或者通过业务后端监控数据库中图表生成服务的状态，来确定生成结果是否可用。若生成结果可用，前端即可获取并处理相应的数据，最终将结果返回给客户端展示。(在此期间，用户可以去做自己的事情)

:::

![](https://cdn.nlark.com/yuque/0/2024/png/44351073/1723019672128-33363dee-5563-4326-9521-c023502d1092.png)



# 五、技术栈
前端:

+ React
+ 开发框架 Umi + Ant Design Pro
+ 可视化开发库(Echarts + Highcharts + AntV)<可视化会涉及到图表的生成>
+ umi openapi 代码生成(自动生成后端调用代码)<前后联调开发>



后端:

+ Spring Boot (万用 Java 后端项目模板，快速搭建基础框架，避免重复写代码)
+ MySQL数据库
+ MyBatis Plus数据访问框架
+ 消息队列(RabbitMO)
+ AI 能力(Open Al接口开发/星球提供现成的 AI接口)
+ Excel 的上传和数据的解析(Easy Excel)
+ Swagger + Knife4j 项目接囗文档
+ Hutool 工具库



# 六、功能开发
### 1.初始化项目框架
### 2.智能分析业务开发
#### 业务流程：
1. 用户输入
    - 输入分析目标
    - 输入需要分析的图表类型
    - 选择上传原数据文件
2. 后端校验
    - 用户输入是否合法
    - 成本控制（统计次数和校验、鉴权）
    - 表格处理

3.AI调用

    - 将用户输入字符与预设好提问词的Ai拼接返回所需数据

#### 接口开发：
允许用户通过此接口上传图表。

:::tips
+ **URL**：`/chart/gen`
+ **Method**：`Post`
+ **需要登录**：<font style="background:#F6E1AC;color:#664900">是</font>
+ **需要鉴权**：<font style="background:#F6E1AC;color:#664900">是</font>
+ **请求参数：**MultipartFile，ChartgetRequest

:::

---

1. 步骤一：补全数据库字段，添加name字段



2. 步骤二：使用EasyExecel，编写ExeclUtils工具类

```java
public static String excelToCsv(MultipartFile multipartFile) {

List<Map<Integer, String>> list = null;

try {
    list = EasyExcel.read(multipartFile.getInputStream())
    .excelType(ExcelTypeEnum.XLSX)
    .sheet()
    .headRowNumber(0)
    .doReadSync();
} catch (IOException e) {
    log.error("表格处理错误",e);
}

if (CollUtil.isEmpty(list)) {
    return null;
}
//转换为csv
// 创建StringBuilder对象用于构建字符串（提高拼接效率）
StringBuilder stringBuilder = new StringBuilder();

// 读取表头信息
LinkedHashMap<Integer, String> stringLinkedMap = (LinkedHashMap) list.get(0);
// 过滤掉空的表头项，并收集到一个新列表中
List<String> list2 = stringLinkedMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
// 将表头项用逗号拼接，并追加到stringBuilder中
stringBuilder.append(StringUtils.join(list2,","));

// 读取数据行信息
// 从第二个元素开始遍历list，假设其余元素为数据行
for (int i = 1; i < list.size(); i++) {
    // 将每个数据行转换为LinkedHashMap
    LinkedHashMap<Integer, String> map = (LinkedHashMap) list.get(i);
    // 过滤掉空的数据项，并收集到一个新列表中
    List<String> list1 = map.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
    // 将数据项用逗号拼接，并追加到stringBuilder中，每行数据后追加换行符
    stringBuilder.append(StringUtils.join(list1,",")).append("\n");
}

// 返回构建完成的字符串
return stringBuilder.toString();


}
```



3. 步骤三
+  预设Ai提问词，控制	输入格式，返回符合要求的数据，例如：

![](https://cdn.nlark.com/yuque/0/2024/png/44351073/1723597911017-cdc31eae-2df6-4363-8de0-7b2d046e38ed.png)

+ 为了让 AI 更好地理解我们的输入，并给出预期精确的输出，需要严格控制我们的提问词。如下：

![](https://cdn.nlark.com/yuque/0/2024/png/44351073/1723621266736-d1f61fef-98c8-4262-a4bb-4af396c267e7.png)





+ 导入AI-SDK依赖

```java
<dependency>
<groupId>com.yucongming</groupId>
<artifactId>yucongming-java-sdk</artifactId>
<version>0.0.4</version>
</dependency>
```



+ 封装工具类

```java
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    public String doChat(Long id,String message){

        DevChatRequest chatRequest = new DevChatRequest();
        chatRequest.setModelId(id);
        chatRequest.setMessage(message);

        BaseResponse<DevChatResponse> responseBaseResponse = yuCongMingClient.doChat(chatRequest);

        ThrowUtils.throwIf(responseBaseResponse == null, ErrorCode.SYSTEM_ERROR,"AI 响应错误");
        return responseBaseResponse.getData().getContent();

    }
}
```

+ 封装返回给前端包装类

```java
@Data
public class BiResponse implements Serializable {
    private String genChart;
    private String genResult;
    private Long chartId;

    
}
```

+ 将用户输入的数据进行拼接



```java
public BaseResponse<BiResponse> intelGetByAi(MultipartFile multipartFile, ChartgetRequest chartgetRequest, HttpServletRequest request) {
    String goal = chartgetRequest.getGoal();
    String name = chartgetRequest.getName();
    String chartType = chartgetRequest.getChartType();
    //校验
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR,"目标为空！");
    ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() >100 ,ErrorCode.PARAMS_ERROR,"字符长度不能大于100！");

    //拼接用户输入
    StringBuilder userInput = new StringBuilder();

    userInput.append("分析需求:").append("\n");
    //拼接分析目标
    if (StrUtil.isNotBlank(chartType)) {
        goal+= goal + ",请使用" + chartType;
    }
    userInput.append(goal).append("\n");
    userInput.append("原始数据").append("\n");
    //拼接分析数据
    String toCsv = ExeclUtils.excelToCsv(multipartFile);
    userInput.append(toCsv).append("\n");


    String result = aiManager.doChat(AI_ID, userInput.toString());

    String[] split = result.split("【【【【【");

    if (split.length < 3) {
        throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"AI生成错误");
    }
    //获取当前登录用户
    User loginUser = userService.getLoginUser(request);

    String genChart = split[1].trim();
    String genResult = split[2].trim();
    Chart chart = new Chart();
    chart.setGoal(goal);
    chart.setName(name);
    chart.setChartData(toCsv);
    chart.setChartType(chartType);
    chart.setGenChart(genChart);
    chart.setGenResult(genResult);
    chart.setUserId(loginUser.getId());

    boolean save = save(chart);
    ThrowUtils.throwIf(!save,ErrorCode.SYSTEM_ERROR,"图表保存失败");
    BiResponse biResponse = new BiResponse();
    biResponse.setGenChart(chart.getGenChart());
    biResponse.setGenResult(chart.getGenResult());
    biResponse.setChartId(chart.getId());

    return ResultUtils.success(biResponse);
}
```



# 七、系统优化
## 现有问题
+ 若有用户故意上传超大文件，攻击网站,网站不安全
+ 目前所有生成图表都会插入到chart表中，若将来数据量变大，查询效率慢，用户体验下降
+ 成本问题，用户每调用一次AIGC就会消耗tokens，真正上线的时候，需要限制用户使用次数。若用户频繁发起请求，会给服务器带来压力

## 解决方案
针对第一个问题：做简单的文件校验

针对第三个问题：使用限流的思想

## 3.优化业务实现
问题场景：面临服务处理能力有限，或者接口处理(或返回)时长较长时，就应该考虑采用异步化。

具体来看，我们可能会遇到以下问题：

1. 用户等待时间过长：这是因为需要等待 AI 生成结果。

2. 业务服务器可能面临大量请求处理，导致系统资源紧张，严重时甚至可能导致服务器宕机或无法处理新的请求。

3. 调用的第三方服务(AI 能力)的处理能力有限。比如每 3 秒只能处理 1 个请求，就会导致 AI 处理不过来；严重时，AI 可能会对我们的后台系统拒绝服务。

综上所述，面对这些问题，我们应当考虑异步化的解决方案。

#### 
# 八、系统展示
![](https://cdn.nlark.com/yuque/0/2024/png/44351073/1725937723832-7d90b690-0032-46a0-ba0c-46ced5fdf898.png)

![](https://cdn.nlark.com/yuque/0/2024/png/44351073/1725937709726-850b939d-bbc5-4c2b-adc9-56f59f6082a2.png)

## 


## 
## 




