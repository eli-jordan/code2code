package stories.creatinggenerators;import org.jbehave.scenario.Scenario;import steps.CreateGeneratorSteps;import steps.GenerateFilesSteps;import steps.ProjectSteps;public class SetParamsForGenerators extends Scenario {		public SetParamsForGenerators() {       super(new ProjectSteps(), new CreateGeneratorSteps(), new GenerateFilesSteps());    }	}