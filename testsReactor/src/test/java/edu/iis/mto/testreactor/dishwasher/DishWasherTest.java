package edu.iis.mto.testreactor.dishwasher;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;

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

        Assert.assertEquals(expectedStatus, runResult.getStatus());
    }

    @Test
    public void fullFilterShouldResultInErrorFilterStatus() {
        Mockito.when(door.closed()).thenReturn(true);
        Mockito.when(dirtFilter.capacity()).thenReturn(49.9);

        RunResult runResult = dishWasher.start(exampleProperProgramConfiguration);

        Status expectedStatus = Status.ERROR_FILTER;

        Assert.assertEquals(expectedStatus, runResult.getStatus());
    }
}
