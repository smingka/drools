package com.ikea.spring.drools.api.service

import com.ikea.spring.drools.api.model.OrderMessage
import org.kie.api.runtime.KieContainer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class OrderValidationService(private val kieContainer: KieContainer) {

    private val logger =
        LoggerFactory.getLogger("DroolsRules")

	private val log = LoggerFactory.getLogger(javaClass)

	fun validate(order: OrderMessage): OrderMessage {
		normalize(order)
		val session = kieContainer.newKieSession()
		try {

            // injecting DRL global BEFORE firing rules
            session.setGlobal("logger", logger)

			session.insert(order)
            val firedRules = session.fireAllRules()
			order.applyValidationStatus()
			log.info(
				"Drools validation completed. orderId={}, status={}, reasons={}, rulesFired={}",
				order.orderId,
				order.validationStatus,
				order.rejectionReasons,
                firedRules

			)
            logger.info(
                "Drools validation completed. orderId={}, rulesFired={}",
                order.orderId,
                firedRules
            )
			return order
		} finally {
			session.dispose()
		}
	}

	private fun normalize(order: OrderMessage) {
		order.orderId = order.orderId?.trim()
		order.customerName = order.customerName?.trim()
		order.country = order.country?.trim()?.uppercase(Locale.ROOT)
		order.orderType = order.orderType?.trim()?.uppercase(Locale.ROOT)
	}
}
