import { langByEngine } from "../langs";

test("language support by engine name", () => {
  expect(langByEngine("groovy").language.name).eq("groovy");
  expect(langByEngine("ruby").language.name).eq("ruby");
  expect(langByEngine("jruby").language.name).eq("ruby");
  expect(langByEngine("kotlin").language.name).eq("kotlin");

  expect(() => langByEngine("non-existing-support")).toThrowError("Invalid language");
});
