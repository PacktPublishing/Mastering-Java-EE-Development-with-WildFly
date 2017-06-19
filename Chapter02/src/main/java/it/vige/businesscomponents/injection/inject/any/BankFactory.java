package it.vige.businesscomponents.injection.inject.any;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

public class BankFactory {

    @Produces
    @BankProducer
    public Bank createBank(@Any Instance<Bank> instance, InjectionPoint injectionPoint) {

        Annotated annotated = injectionPoint.getAnnotated();
        BankType bankTypeAnnotation = annotated.getAnnotation(BankType.class);
        Class<? extends Bank> bankType = bankTypeAnnotation.value().getBankType();
        return instance.select(bankType).get();
    }
}
