const Sequelize = require("sequelize");

module.exports = class User extends Sequelize.Model {
  // 테이블에 대한 설정
  static init(sequelize) {
    return super.init(
      {
        // 컬럼에 대한 설정
        // 카카오로그인이 있을 수 있기 때문에 email, password는 null일 수 있다.
        // 그렇지만 카카오로그인에서도 nick은 있기 때문에 nick은 null일 수 없다.
        email: {
          type: Sequelize.STRING(40),
          allowNull: true,
          unique: true,
        },
        nick: {
          type: Sequelize.STRING(40),
          allowNull: false,
        },
        password: {
          type: Sequelize.STRING(128),
          allowNull: true,
        },
        provider: {
          type: Sequelize.STRING(10),
          allowNull: false,
          defaultValue: "local",
        },
        snsId: {
          type: Sequelize.STRING(50),
          allowNull: true,
        },
      },
      {
        // 테이블에 대한 설정
        sequelize,
        timestamps: true,
        underscored: false,
        modelName: "User",
        tableName: "snsuser",
        paranoid: true,
        charset: "utf8",
        collate: "utf8_general_ci",
      }
    );
  }

  // 관계에 대한 설정
  static associate(db) {
    db.User.hasMany(db.Post);
    db.User.belongsToMany(db.User, {
      foreignKey: "followingId",
      as: "Followers",
      through: "Follow",
    });
    db.User.belongsToMany(db.User, {
      foreignKey: "followerId",
      as: "Followings",
      through: "Follow",
    });
  }
};
