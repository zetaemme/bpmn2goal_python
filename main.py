from src.bpmn_loader import BpmnLoader


def main() -> None:
    pass


if __name__ == "__main__":
    loader = BpmnLoader()
    loader.load("data/lims.bpmn")

    workflow = loader.extract_workflow()

    if workflow is not None:
        print("Workflow extracted successfully")
