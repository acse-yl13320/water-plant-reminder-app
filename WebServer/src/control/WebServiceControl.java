package control;

import entity.Plant;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class WebServiceControl {

    @WebMethod
    public String plantService(@WebParam(name = "plantName") String name){
        Plant plant = PlantControl.getPlant(name);
        return plant.toString();
    }
}
