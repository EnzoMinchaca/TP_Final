/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpfinal;

import java.util.Date;

/**
 *
 * @author Enzo
 */
public class Inscripcion {
    Alumno alumno;
    Materia materia;
    Date fecha = new Date();
    boolean aprobada;

    public Inscripcion() {
    }

    public Inscripcion(Alumno alumno, Materia materia, boolean aprobada) {
        this.alumno = alumno;
        this.materia = materia;
        this.aprobada = aprobada;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void aprobada(Alumno alumno, Materia materia) {
        if(alumno.getMateriasAprobadas().containsAll(materia.getCorrelativas())) {
            System.out.println("Aprobado\n");
        } else {
            System.out.println("Desaprobado\n");
        }
    }
    
    @Override
    public String toString() {
        return "Inscripcion{" + "materia=" + materia + ", alumno=" + alumno + ", fecha=" + fecha + '}';
    }
}
