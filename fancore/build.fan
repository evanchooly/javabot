using build

class Build : BuildPod
{

  override Void setup()
  {
    podName = "fancore"
    version = Version("3.0.5")
  }
}