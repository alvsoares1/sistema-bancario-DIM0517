package com.example.banco.exception;

public class MinFundsExceededException extends BussinesException {

  public MinFundsExceededException() {
    super("Saldo ultrapassa o valor limite do crédito especial.");
  }
}
