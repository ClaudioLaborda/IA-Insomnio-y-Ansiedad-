package tuti.daos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;



/**
 * Integrantes
 * Claudio Laborda - claborda@frsf.utn.edu.ar
 * Cristian Nozralah - cnozralah@frsf.utn.edu.ar
 * Lucas Rojas - lrojas@frsf.utn.edu.ar
 * Méndez Nicolás - Nicomendbc03@gmail.com
 */
@SpringBootApplication
@EntityScan("tuti.daos.entidades")
public class InsomnioyAnsiedadApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsomnioyAnsiedadApplication.class, args);
    }
}