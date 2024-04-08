import configparser
import os

__all__ = [
    "CFG_PATH", "LLM_OPENAI_KEY",
    "JTYPE_PROVIDER_JAR_PATH", "JTYPE_PROVIDER_API_NAME"
]

# llm-seed-generator.cfg
CFG_PATH = os.path.join(os.path.dirname(os.path.dirname(__file__)),"llm-seed-generator.cfg")
CONFIG = configparser.ConfigParser()
CONFIG.read(CFG_PATH)

LLM_OPENAI_KEY = CONFIG.get("LLM", "OPENAI_KEY")
os.environ["OPENAI_API_BASE"] = "https://api.kwwai.top/v1"
os.environ["OPENAI_API_KEY"] = LLM_OPENAI_KEY

JTYPE_PROVIDER_JAR_PATH = CONFIG.get("JTYPE_PROVIDER", "JAR_PATH")
JTYPE_PROVIDER_API_NAME = CONFIG.get("JTYPE_PROVIDER", "API_FULL_QUALIFIED_CLASS_NAME")
