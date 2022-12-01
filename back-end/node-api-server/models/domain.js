const Sequelize = require("sequelize");
module.exports = class Domain extends Sequelize.Model {
  // 테이블에 대한 설정
  static init(sequelize) {
    return super.init(
      {
        host: {
          type: Sequelize.STRING(100),
          allowNull: false,
        },
        clientSecret: {
          type: Sequelize.STRING(36),
          allowNull: false,
        },
        type: {
          type: Sequelize.ENUM("free", "premium"),
          allowNull: false,
        },
      },
      {
        // 테이블에 대한 설정
        sequelize,
        timestamps: true,
        underscored: false,
        modelName: "Domain",
        tableName: "domains",
        paranoid: true,
      }
    );
  }

  static associate(db) {
    // User의 기본키가 Domain의 외래키로 추가
    db.Domain.belongsTo(db.User);
  }
};
