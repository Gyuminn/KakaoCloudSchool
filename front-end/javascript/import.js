import { cube, multiplyPIwithSQRT2, graph } from "./export.js";

graph.options = {
  color: "blue",
  thickness: "3px",
};

graph.draw();

console.log(cube(3));

console.log(multiplyPIwithSQRT2);
