package edu.iis.mto.testreactor.dishwasher;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.engine.EngineException;
import edu.iis.mto.testreactor.dishwasher.pump.PumpException;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

public class DishWasherTest {

    private WaterPump waterPump;
    private Engine engine;
    private DirtFilter dirtFilter;
    private Door door;

    DishWasher dishWasher;
    private final ProgramConfiguration exampleProperProgramConfiguration = ProgramConfiguration.builder()
                                                                                               .withProgram(WashingProgram.ECO)
                                                                                               .withFillLevel(FillLevel.HALF)
                                                                                               .withTabletsUsed(true)
                                                                                               .build();

    @Before
    public void init() {
        waterPump = Mockito.mock(WaterPump.class);
        engine = Mockito.mock(Engine.class);
        dirtFilter = Mockito.mock(DirtFilter.class);
        door = Mockito.mock(Door.class);

        dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    public void openDoorShouldResultInDoorOpenStatus() {
        Mockito.when(door.closed()).thenReturn(false);

        RunResult runResult = dishWasher.start(exampleProperProgramConfiguration);
        Status expectedStatus = Status.DOOR_OPEN;

        assertEquals(expectedStatus, runResult.getStatus());
    }

    @Test
    public void fullFilterShouldResultInErrorFilterStatus() {
        Mockito.when(door.closed()).thenReturn(true);
        Mockito.when(dirtFilter.capacity()).thenReturn(49.9);

        RunResult runResult = dishWasher.start(exampleProperProgramConfiguration);

        Status expectedStatus = Status.ERROR_FILTER;

        assertEquals(expectedStatus, runResult.getStatus());
    }

    @Test
    public void methodsShouldBeCalledInRightOrder() throws PumpException, EngineException {
        Mockito.when(door.closed()).thenReturn(true);
        Mockito.when(dirtFilter.capacity()).thenReturn(60.5);

        InOrder inOrder = Mockito.inOrder(door, waterPump, engine);

        dishWasher.start(exampleProperProgramConfiguration);

        inOrder.verify(door).closed();
        inOrder.verify(waterPump).pour(Mockito.any(FillLevel.class));
        inOrder.verify(engine).runProgram(Mockito.any(WashingProgram.class));
        inOrder.verify(waterPump).drain();
        inOrder.verify(door).unlock();
    }

    @Test
    public void successfulWashingShouldResultInSuccessStatus() {
        Mockito.when(door.closed()).thenReturn(true);
        Mockito.when(dirtFilter.capacity()).thenReturn(60.5);

        RunResult runResult = dishWasher.start(exampleProperProgramConfiguration);

        Status expectedStatus = Status.SUCCESS;

        assertEquals(expectedStatus, runResult.getStatus());
    }

    @Ignore
    public void successfulWashingShouldRunProgramOnlyOnce() throws  PumpException {
        Mockito.when(door.closed()).thenReturn(true);
        Mockito.when(dirtFilter.capacity()).thenReturn(60.5);

        dishWasher.start(exampleProperProgramConfiguration);

        Mockito.verify(waterPump, Mockito.times(1))
               .pour(Mockito.any(FillLevel.class));
    }

    @Test
    public void shouldReturnProperMinutes(){
        Mockito.when(door.closed()).thenReturn(true);
        Mockito.when(dirtFilter.capacity()).thenReturn(60.5);

        RunResult runResult = dishWasher.start(exampleProperProgramConfiguration);

        int expectedMinutes = 90;

        assertEquals(expectedMinutes, runResult.getRunMinutes());
    }
}
