import http from 'k6/http';
import { check } from 'k6';
import { Trend } from 'k6/metrics';

export let options = {
  vus: 1000,
  duration: '30s',
};

let responseTime = new Trend('response_time');

export default function () {
  const payload = JSON.stringify({
    apiMethod: "POST",
    requestDTO: {
      url: "http://httpbin.org/post", // test target
      headerVariables: {
        Authorization: "Bearer dummy_token"
      },
      params: [
        { name: "key1", value: "value1" }
      ],
      bodyType: "application/json",
      requestBody: "{\"key\": \"value\"}"
    },
    timeout: 5000
  });

  const headers = {
    'Content-Type': 'application/json'
  };

  const res = http.post('http://localhost:8080/aysncOrchestrationApi', payload, { headers });

  responseTime.add(res.timings.duration);
  console.log("status-->",res.status)

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500
  });
}
