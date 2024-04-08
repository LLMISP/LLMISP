import json
import jpype

from config import *


class JtypeProvider:
    """
    Used to initialize run llm-jtype-provider project.

    * LLMFuzz needs a method to initialize sources.
    * LLMFuzz requires an interface to get Dependency-Graph of a Java type.
    """
    def __init__(self):
        jpype.startJVM(
            jpype.getDefaultJVMPath(), "-ea",
            f"-Djava.class.path={JTYPE_PROVIDER_JAR_PATH}")
        self.jp_api = jpype.JClass(JTYPE_PROVIDER_API_NAME)

    def __del__(self):
        try:
            if jpype.isJVMStarted():
                jpype.shutdownJVM()
        except Exception as e:
            print(e)

    def initialize(self, jar_gav: str) -> None:
        self.jp_api.initialize(jar_gav)

    def get_method_graph(self, signature: str, level: int = 1):
        return self.jp_api.getMethodGraphBySignature(signature, jpype.JInt(level))

    def to_json(self, method_graph, level: int = 1) -> dict:
        json_str: str = str(self.jp_api.toJSONString(method_graph, jpype.JInt(level)))
        return json.loads(json_str.replace("\n", ""))
