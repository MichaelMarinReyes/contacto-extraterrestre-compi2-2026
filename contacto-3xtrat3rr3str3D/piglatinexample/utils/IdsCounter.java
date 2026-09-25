/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author blue-dragon
 */
@Getter
public class IdsCounter {
    private Integer temporalCount;
    private Integer labelCount;

    public IdsCounter() {
        this.temporalCount = 0;
        this.labelCount = 0;
    }
    
    public void incrementTemporalCount(){
        temporalCount++;
    };
    
    public void incrementLabelCount(){
        labelCount++;
    }
    
    public int getAndIncrementTemporalCount(){
        temporalCount ++;
        return temporalCount - 1;
    }
    
    public int getAndIncrementLabelCount(){
        labelCount ++;
        return labelCount - 1;
    }
}
