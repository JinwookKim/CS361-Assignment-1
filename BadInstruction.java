/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.simple.security;

/**
 *
 * @author jinwook
 */
public class BadInstruction {
    final private String errorMessage;

    public BadInstruction()
    {

    }

    public BadInstruction(String message)
    {
        errorMessage = message;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
