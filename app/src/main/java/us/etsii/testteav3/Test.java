package us.etsii.testteav3;

import java.util.Date;
import java.util.List;

public class Test {
    //String alumno;
    Date fecha;
    List<Integer> resultado;
    Integer completo;

    public Test(){

    }

    public Test(Integer completo, List<Integer> resultado, Date fecha) {
       // this.alumno = alumno;
        this.fecha=fecha;
        this.resultado = resultado;
        this.completo = completo;
    }

    public List<Integer> getResultado() {
        return resultado;
    }

    public Integer getCompleto() {
        return completo;
    }

    public Date getFecha(){
        return fecha;
    }

   /* public void setAlumno(String alumno) {
        this.alumno = alumno;
    }*/

    public void setResultado(List<Integer> resultado) { this.resultado = resultado; }

    public void setCompletado(Integer completado) {
        this.completo = completado;
    }

    public void setFecha(Date fecha){this.fecha=fecha;}
}
