from src.bpmn_loader import BpmnLoader
from src.workflow import Task, WorkflowState


def main() -> None:
    pass


if __name__ == "__main__":
    loader = BpmnLoader()
    loader.load("data/lims.bpmn")

    workflow = loader.extract_workflow()

    if workflow is not None:
        workflow_state = WorkflowState(workflow)
        result = ""
        # NOTE: Remove comment to separate the results and work with them as a structured Python object
        # results: dict[str, list] = {
        #     "goals": [],
        #     "preconditions": [],
        #     "postconditions": []
        # }

        for item in workflow.items:
            if isinstance(item, Task):
                precondition = workflow_state.goal_trigger_condition(item)
                postcondition = workflow_state.goal_final_state(item)

                # NOTE: Remove comment to separate the results and work with them as a structured Python object
                # results["goals"].append(item.label.replace("\n", " "))
                # results["preconditions"].append(precondition.prettify())
                # results["postconditions"].append(postcondition.prettify())

                precondition_str = precondition.prettify() if precondition is not None else "None"
                postcondition_str = postcondition.prettify() if postcondition is not None else "None"
                result += f"{item.label.replace('\n', ' ')} [PRE: {precondition_str}, POST: {postcondition_str}]\n"

        print(result)
