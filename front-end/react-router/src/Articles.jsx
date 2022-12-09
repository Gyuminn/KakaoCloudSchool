import { Link } from "react-router-dom";

const Articles = () => {
  return (
    <ul>
      <li>
        <Link to="article/1">
          손으로 코딩하고 뇌로 컴파일하며 눈으로 디버깅한다.
        </Link>
      </li>
      <li>
        <Link to="article/2">
          node의 프레임워크로는 express와 nest가 있고, koa도 있다.
        </Link>
      </li>
      <li>
        <Link to="article/3">
          Java의 프레임워크로는 Spring Boot가 대세이다.
        </Link>
      </li>
      <li>
        <Link to="article/4">React의 프레임워크로는 Next.js가 대세이다.</Link>
      </li>
    </ul>
  );
};

export default Articles;
