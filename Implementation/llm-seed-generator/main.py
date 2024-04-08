from model.v2.llm_generator import LLMGenerator
import sys
import json
import util.file_util

if __name__ == "__main__":

    skip = str
    if len(sys.argv) > 1:
        skip = sys.argv[1]

    file = open("graph.json", "r")
    file_content = file.read()
    file.close()
    mg_dict = json.loads(file_content.replace("\n", ""))

    if mg_dict["static"] is False:
        class_name = mg_dict["className"]
        class_node = mg_dict["nodes"][class_name]
        if len(class_node.get("constructors", "")) == 0 and len(class_node.get("builders", "")) == 0:
            raise Exception("Cannot initiate class object.")

    generator = LLMGenerator(temperature=0.0)
    if skip == "skipEP":
        output = generator.generate_non_ep(mg_dict)
    elif skip == "skipUnder":
        output = generator.generate_non_understanding(mg_dict)
    elif skip == "basic":
        output = generator.generate_basic(mg_dict)
    else:
        output = generator.generate(mg_dict)
    util.file_util.write_dict(output, mg_dict["static"])
