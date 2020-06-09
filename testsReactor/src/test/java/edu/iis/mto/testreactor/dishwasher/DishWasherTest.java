package edu.iis.mto.testreactor.dishwasher;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;

public class DishWasherTest {

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    public void openDoorShouldResultInDoorOpenStatus() {
        WaterPump waterPump = Mockito.mock(WaterPump.class);
        Engine engine = Mockito.mock(Engine.class);
        DirtFilter dirtFilter = Mockito.mock(DirtFilter.class);
        Door door = Mockito.mock(Door.class);

        ProgramConfiguration exampleProperProgramConfiguration = ProgramConfiguration.builder()
                                                                                     .withProgram(WashingProgram.ECO)
                                                                                     .withFillLevel(FillLevel.HALF)
                                                                                     .withTabletsUsed(true)
                                                                                     .build();

        DishWasher dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);

        Mockito.when(door.closed()).thenReturn(false);

        RunResult runResult = dishWasher.start(exampleProperProgramConfiguration);
        Status expectedStatus = Status.DOOR_OPEN;
        Assert.assertEquals(runResult.getStatus(), expectedStatus);

    }
}
