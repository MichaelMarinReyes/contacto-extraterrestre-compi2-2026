/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.utils;

import igriega.piglatin.cuartetas.Cuarteta;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author blue-dragon
 */
@Getter
public class IntermediateCodeUtils {  

    public IntermediateCodeUtils() {
        this.cuartetas = new ArrayList<>();
        this.counter = new IdsCounter();
    }
    
    private List<Cuarteta> cuartetas;
    private IdsCounter counter;
    
}
