class Build : build::BuildPod
{
  new make()
  {
    podName = "fantom-core"
    summary = "javabot fantom-based core"
    depends = ["sys 1.0", "inet 1.0", "concurrent 1.0"]
    srcDirs = [`src/main/fantom/`, `src/test/fantom/`]
  }
}