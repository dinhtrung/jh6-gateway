GET / azlog - * /_search
{
  "size": 0,
    "aggs": {
        "uniq_gender": {
            "terms": {
                "field": "headers.x_consumer_username.keyword"
            }
        }
    }
}


{
  "localdate" : "201911261900",
  "createdDate" : "2019-04-01T14:17:24.466Z",
  "@version" : "1",
  "level" : 80,
  "tags" : [
    "http_request"
  ],
  "headers" : {
    "content_type" : "application/json;charset=utf-8",
    "http_host" : "localhost:8081",
    "x_consumer_id" : "dd543077-dec7-58ed-9209-c8c7c50c4e13",
    "x_forwarded_port" : "8000",
    "x_consumer_username" : "gamebai",
    "http_user_agent" : "axios/0.19.0",
    "content_length" : "435",
    "http_version" : "HTTP/1.1",
    "request_method" : "GET",
    "x_forwarded_for" : "42.118.218.138",
    "x_forwarded_host" : "logstash.azlogger.com",
    "request_path" : "/gamebai?apikey=513f26545545cc00f75019f23ff1e5e3",
    "x_real_ip" : "42.118.218.138",
    "x_forwarded_proto" : "http",
    "connection" : "keep-alive",
    "http_accept" : "application/json, text/plain, */*"
  },
  "ad_where" : "Future Integration Agent",
  "ad_status" : "Success",
  "createdAccountDate" : "2019-01-02T20:30:02.142Z",
  "eCPM" : 74157,
  "ad_instance_name" : "AdMob",
  "ad_instance_id" : "hard drive",
  "@timestamp" : "2019-11-26T19:00:21.284Z",
  "ad_source" : "AdMob",
  "country" : "FK",
  "gameId" : "com.helena",
  "code" : 71470,
  "host" : "127.0.0.1",
  "appVersion" : "2.2",
  "dateISO" : "2019-11-26T16:46:01.817Z",
  "type" : "RewardedVideo",
  "ad_session_id" : "446978cb-2d0f-4506-8802-b8d0a7382047"
}
