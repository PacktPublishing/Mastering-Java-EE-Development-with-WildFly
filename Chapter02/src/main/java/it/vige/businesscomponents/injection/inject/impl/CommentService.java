package it.vige.businesscomponents.injection.inject.impl;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;

import it.vige.businesscomponents.injection.inject.Service;

@Any
@SessionScoped
public class CommentService implements Service, Serializable {

	private static final long serialVersionUID = 8848734789369881623L;

}
