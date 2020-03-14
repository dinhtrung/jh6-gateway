package com.ft.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/public")
public class OamManagementResource {
	
	private final Logger log = LoggerFactory.getLogger(OamManagementResource.class);

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Handle webhook from Prometheus AlertManager
	 * {
	 *   "receiver":"alerta",
	 *   "status":"firing",
	 *   "alerts":
	 *     [{
	 *       "status":"firing",
	 *       "labels":{"alertname":"SystemdServiceCrashed","instance":"localhost:9100","job":"node","name":"chronyd.service","severity":"warning","state":"failed","type":"forking"},
	 *       "annotations":{"description":"SystemD service crashed\n  VALUE = 1\n  LABELS: map[__name__:node_systemd_unit_state instance:localhost:9100 job:node name:chronyd.service state:failed type:forking]",
	 *       "summary": "SystemD service crashed (instance localhost:9100)"},
	 *       "startsAt": "2020-03-12T11:42:00.594473624+07:00",
	 *       "endsAt": "0001-01-01T00:00:00Z",
	 *       "generatorURL":"http://dinhtrung-fedora:9090/graph?g0.expr=node_systemd_unit_state%7Bstate%3D%22failed%22%7D+%3D%3D+1\u0026g0.tab=1",
	 *       "fingerprint":"a171ce41daf961fb"
	 *       }
	 *       ,{"status":"firing","labels":{"alertname":"SystemdServiceCrashed","instance":"localhost:9100","job":"node","name":"logrotate.service","severity":"warning","state":"failed","type":"oneshot"},"annotations":{"description":"SystemD service crashed\n  VALUE = 1\n  LABELS: map[__name__:node_systemd_unit_state instance:localhost:9100 job:node name:logrotate.service state:failed type:oneshot]","summary":"SystemD service crashed (instance localhost:9100)"},"startsAt":"2020-03-12T11:42:00.594473624+07:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://dinhtrung-fedora:9090/graph?g0.expr=node_systemd_unit_state%7Bstate%3D%22failed%22%7D+%3D%3D+1\u0026g0.tab=1","fingerprint":"3bbd27577f609e19"}
	 *   ],
	 *  "groupLabels":{"alertname":"SystemdServiceCrashed"},
	 *  "commonLabels":{"alertname":"SystemdServiceCrashed","instance":"localhost:9100","job":"node","severity":"warning","state":"failed"},
	 *  "commonAnnotations":{"summary":"SystemD service crashed (instance localhost:9100)"},
	 *  "externalURL":
	 *  "http://dinhtrung-fedora:9093",
	 *  "version":"4",
	 *  "groupKey":"{}:{alertname=\"SystemdServiceCrashed\"}"
	 * }
	 * @return
	 */
	@PostMapping("/alert-manager-webhook")
	public ResponseEntity<String> onAlertManagerWebhook(@RequestBody String webhook) {
		log.debug("+ webhook: {}", webhook);
		return ResponseEntity.accepted().body(null);
	}
}
