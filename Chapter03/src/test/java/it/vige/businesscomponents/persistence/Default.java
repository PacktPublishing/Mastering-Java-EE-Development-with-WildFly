package it.vige.businesscomponents.persistence;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;

import java.io.File;

import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class Default {

	public static JavaArchive createJavaArchive(String name, Package... packages) {
		final JavaArchive jar = create(JavaArchive.class, name);
		for (Package packagev : packages)
			jar.addPackage(packagev);
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/persistence-test.xml")),
				"persistence.xml");
		return jar;
	}
}
