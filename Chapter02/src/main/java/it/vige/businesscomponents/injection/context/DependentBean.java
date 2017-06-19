package it.vige.businesscomponents.injection.context;

import javax.enterprise.context.Dependent;

@Dependent
public class DependentBean extends CountBean implements Count<Integer> {

}