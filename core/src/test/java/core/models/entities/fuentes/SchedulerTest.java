package core.models.entities.fuentes;

import core.models.agregador.SchedulerAgregador;

public class SchedulerTest {
    public static void main(String[] args) throws Exception {
        SchedulerAgregador scheduler = new SchedulerAgregador(false);
        scheduler.iniciarScheduler();}}
