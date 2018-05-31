/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model;

/**
 *
 * @author mirek
 */
public class RedirectAttribute {
    private Integer counter = 0;
    private Object value;
    public RedirectAttribute(Object value) {
        this.value = value;
    }
    private void incrementCounter(){
        this.counter++;
    }
    public Integer getCounter(){
        return this.counter;
    }
    public Boolean toRemove(){
        return this.counter>=1;
    }
    public void setValue(Object value){
        this.value = value;
    }
    public Object getValue(){
        incrementCounter();
        return this.value;
    }
    @Override
    public String toString() {
        return "RedirectAttribute{" + "counter=" + counter + ", value=" + value + '}';
    }
    
}
