const express = require("express");
const User = require("../models/user");
const { isLoggedIn } = require("./middlewares");

const router = express.Router();

router.post("/:id/follow", isLoggedIn, async (req, res, next) => {
  try {
    // 현재 로그인한 유저를 찾는다.
    const user = await User.findOne({ where: { id: req.user.id } });

    if (user) {
      // 팔로우로 추가
      await user.addFollowing(parseInt(req.params.id, 10));
      res.send("success");
    } else {
      res.status(404).send("no user");
    }
  } catch (error) {
    console.error(error);
    next(error);
  }
});

module.exports = router;
