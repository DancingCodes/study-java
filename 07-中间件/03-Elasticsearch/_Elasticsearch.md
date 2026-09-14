# 第七阶段 · 第三课：Elasticsearch

---

## 一、Elasticsearch 简介

### 什么是 Elasticsearch？

Elasticsearch（简称 ES）是一个基于 **Lucene** 的分布式搜索引擎，特点：
- **全文搜索**：中文分词、模糊搜索、高亮显示
- **近实时**：数据写入后约 1 秒即可搜索到
- **分布式**：天然支持集群和水平扩展
- **RESTful API**：所有操作都是 HTTP 接口

### 使用场景

| 场景 | 说明 |
|------|------|
| 商品搜索 | 淘宝、京东的搜索框 |
| 日志分析 | ELK（Elasticsearch + Logstash + Kibana） |
| 数据分析 | 聚合统计、指标计算 |
| 自动补全 | 搜索建议、拼音搜索 |

### ES vs MySQL 搜索

| 对比 | MySQL LIKE | Elasticsearch |
|------|-----------|---------------|
| 性能 | 全表扫描，大数据量很慢 | 倒排索引，百万数据毫秒级 |
| 功能 | 简单模糊匹配 | 分词、相关度排序、高亮 |
| 中文 | 不支持分词 | 支持 IK 中文分词 |
| 适用 | 精确查询 | 全文搜索 |

---

## 二、核心概念

### ES 与 MySQL 概念对照

| MySQL | Elasticsearch | 说明 |
|-------|--------------|------|
| Database | Index（索引） | 一类数据的集合 |
| Table | Type（7.x 后废弃） | 文档类型 |
| Row | Document（文档） | 一条数据，JSON 格式 |
| Column | Field（字段） | 数据的属性 |
| Schema | Mapping（映射） | 字段类型定义 |
| SQL | DSL | 查询语法 |

### 倒排索引

传统索引（正向）：文档 → 关键词
倒排索引：**关键词 → 文档列表**

```
文档1: "Spring Boot 入门教程"
文档2: "Spring Cloud 微服务"
文档3: "Redis 入门到精通"

倒排索引：
"Spring"  → [文档1, 文档2]
"入门"    → [文档1, 文档3]
"Boot"    → [文档1]
"Cloud"   → [文档2]
"Redis"   → [文档3]
```

搜索"Spring 入门"→ 交集/并集 → 找到文档1（最相关）

---

## 三、ES 安装

### Docker 安装

```bash
# 拉取 ES 镜像
docker pull elasticsearch:8.11.0

# 创建网络
docker network create elastic

# 启动 ES（单节点，关闭安全认证方便学习）
docker run -d --name es \
  --net elastic \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -e "ES_JAVA_OPTS=-Xms256m -Xmx256m" \
  elasticsearch:8.11.0

# 安装 IK 中文分词器
docker exec -it es bin/elasticsearch-plugin install \
  https://get.infini.cloud/elasticsearch/analysis-ik/8.11.0

# 重启 ES
docker restart es
```

验证：访问 http://localhost:9200 看到版本信息即成功。

---

## 四、ES 基本操作（RESTful API）

### 1. 索引操作

```bash
# 创建索引 + Mapping
PUT /products
{
  "mappings": {
    "properties": {
      "name":  { "type": "text", "analyzer": "ik_max_word" },
      "description": { "type": "text", "analyzer": "ik_max_word" },
      "price": { "type": "double" },
      "stock": { "type": "integer" },
      "category": { "type": "keyword" },
      "createTime": { "type": "date" }
    }
  }
}

# 查看索引
GET /products

# 删除索引
DELETE /products
```

### 字段类型

| 类型 | 说明 | 是否分词 |
|------|------|----------|
| `text` | 文本，会分词 | 是 |
| `keyword` | 关键字，不分词 | 否 |
| `integer/long` | 整数 | 否 |
| `double/float` | 浮点数 | 否 |
| `date` | 日期 | 否 |
| `boolean` | 布尔 | 否 |

### 2. 文档 CRUD

```bash
# 新增文档（指定 ID）
PUT /products/_doc/1
{
  "name": "华为 Mate60 Pro",
  "description": "鸿蒙系统旗舰手机",
  "price": 6999,
  "stock": 100,
  "category": "手机"
}

# 新增文档（自动生成 ID）
POST /products/_doc
{
  "name": "小米14 Pro",
  "description": "骁龙8Gen3旗舰",
  "price": 4999,
  "stock": 200,
  "category": "手机"
}

# 查询文档
GET /products/_doc/1

# 修改文档（局部更新）
POST /products/_update/1
{
  "doc": {
    "price": 6499
  }
}

# 删除文档
DELETE /products/_doc/1
```

### 3. 搜索查询（DSL）

```bash
# 全文搜索（分词匹配）
GET /products/_search
{
  "query": {
    "match": {
      "name": "华为手机"
    }
  }
}

# 精确匹配
GET /products/_search
{
  "query": {
    "term": {
      "category": "手机"
    }
  }
}

# 范围查询
GET /products/_search
{
  "query": {
    "range": {
      "price": { "gte": 3000, "lte": 7000 }
    }
  }
}

# 布尔组合查询
GET /products/_search
{
  "query": {
    "bool": {
      "must": [{ "match": { "name": "手机" } }],
      "filter": [{ "range": { "price": { "lte": 5000 } } }]
    }
  }
}

# 高亮显示
GET /products/_search
{
  "query": { "match": { "name": "华为" } },
  "highlight": {
    "fields": { "name": {} }
  }
}

# 排序 + 分页
GET /products/_search
{
  "query": { "match_all": {} },
  "sort": [{ "price": "asc" }],
  "from": 0,
  "size": 10
}
```

---

## 五、Spring Boot 整合 Elasticsearch

### 1. 添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

### 2. 配置

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
```

### 3. 实体类映射

```java
@Data
@Document(indexName = "products")
public class ProductDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Keyword)
    private String category;
}
```

### 4. Repository 方式（简单查询）

```java
public interface ProductDocRepository extends ElasticsearchRepository<ProductDoc, Long> {

    List<ProductDoc> findByName(String name);

    List<ProductDoc> findByPriceBetween(Double min, Double max);

    List<ProductDoc> findByCategory(String category);
}
```

### 5. ElasticsearchClient 方式（复杂查询）

```java
@Service
@Slf4j
public class ProductSearchService {

    @Autowired
    private ElasticsearchClient esClient;

    public List<ProductDoc> search(String keyword, Double minPrice, Double maxPrice, int page, int size) throws IOException {
        SearchResponse<ProductDoc> response = esClient.search(s -> s
            .index("products")
            .query(q -> q
                .bool(b -> {
                    if (keyword != null) {
                        b.must(m -> m.match(mt -> mt.field("name").query(keyword)));
                    }
                    if (minPrice != null && maxPrice != null) {
                        b.filter(f -> f.range(r -> r
                            .field("price")
                            .gte(JsonData.of(minPrice))
                            .lte(JsonData.of(maxPrice))));
                    }
                    return b;
                }))
            .from(page * size)
            .size(size)
            .highlight(h -> h.fields("name", f -> f)),
            ProductDoc.class);

        return response.hits().hits().stream()
            .map(hit -> {
                ProductDoc doc = hit.source();
                if (hit.highlight().containsKey("name")) {
                    doc.setName(hit.highlight().get("name").get(0));
                }
                return doc;
            })
            .toList();
    }
}
```

---

## 六、数据同步策略

MySQL 是主数据源，ES 是搜索副本，需要保持同步。

| 方式 | 优点 | 缺点 |
|------|------|------|
| **同步调用** | 实现简单，在 Service 中同时写 MySQL 和 ES | 性能差，耦合高 |
| **异步消息** | 通过 MQ 解耦，MySQL 写完发消息，消费者同步 ES | 实现稍复杂，有延迟 |
| **Canal 监听 binlog** | 完全解耦，监听 MySQL 变更自动同步 | 引入新组件，运维成本高 |

**推荐**：中小项目用同步调用或 MQ 异步，大型项目用 Canal。

---

## 七、练习

### 练习 1：商品搜索
实现商品搜索接口：
1. 支持关键词全文搜索（name + description）
2. 支持按分类过滤
3. 支持按价格范围过滤
4. 结果高亮显示

### 练习 2：数据同步
新增商品时同时写入 MySQL 和 ES，删除商品时同时删除。

### 练习 3：聚合统计
统计每个分类下的商品数量和平均价格。
