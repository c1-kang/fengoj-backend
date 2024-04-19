/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3306
 Source Schema         : fengoj

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 08/12/2023 18:43:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '题目内容(json 字段)',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签列表（json 数组）',
  `submitNum` int NOT NULL DEFAULT 0 COMMENT '题目提交数',
  `acceptedNum` int NOT NULL DEFAULT 0 COMMENT '题目通过数',
  `judgeCase` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '判题用例（json 数组）',
  `judgeConfig` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '判题配置（json 对象）',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `level` tinyint NOT NULL COMMENT '困难级别，0-简单，1-中等，2-困难',
  `judgeCaseUrl` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真正判题样例所在地址',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '题目' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (2, 'a+b', '{\"questionDesc\":\"a+b\",\"inputDesc\":\"a+b\",\"outputDesc\":\"a+b\"}', '[\"a+b\"]', 60, 12, '[{\"input\":\"1 2\",\"output\":\"3\",\"desc\":\"\"}]', '{\"timeLimit\":1000,\"memoryLimit\":1000,\"stackLimit\":1000}', 1, '2023-11-20 18:56:37', '2023-12-08 17:49:42', 0, 0, 'E:\\Code\\Project\\OJ\\fengoj-backend\\JudgeCaseDir\\1\\vdIiDRrD');

-- ----------------------------
-- Table structure for question_submit
-- ----------------------------
DROP TABLE IF EXISTS `question_submit`;
CREATE TABLE `question_submit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `language` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编程语言',
  `code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户代码',
  `judgeInfo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '判题信息（json 对象）',
  `status` int NOT NULL DEFAULT 0 COMMENT '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
  `questionId` bigint NOT NULL COMMENT '题目 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_questionId`(`questionId` ASC) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '题目提交' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question_submit
-- ----------------------------
INSERT INTO `question_submit` VALUES (1, 'java', 'import java.util.Scanner;\n\nclass Main {\n    public static void main(String[] args) {\n        Scanner scan = new Scanner(System.in);\n        int a = scan.nextInt();\n        int b = scan.nextInt();\n        System.out.println(a+b);\n    }\n}', '{\"message\":\"Accepted\",\"memory\":0,\"time\":100}', 2, 2, 1, '2023-11-20 18:58:54', '2023-11-20 18:58:56', 0);
INSERT INTO `question_submit` VALUES (2, 'java', 'import java.util.Scanner;\n\nclass Main {\n    public static void main(String[] args) {\n        Scanner scan = new Scanner(System.in);\n        int a = scan.nextInt();\n        int b = scan.nextInt();\n        System.out.println(a+b+b);\n    }\n}', '{\"message\":\"WrongAnswer\",\"memory\":0,\"time\":103}', 2, 2, 1, '2023-11-20 19:42:12', '2023-11-20 19:42:13', 0);
INSERT INTO `question_submit` VALUES (3, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 18:29:21', '2023-12-04 18:29:21', 0);
INSERT INTO `question_submit` VALUES (4, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 18:54:06', '2023-12-04 18:54:07', 0);
INSERT INTO `question_submit` VALUES (5, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 18:58:37', '2023-12-04 18:58:37', 0);
INSERT INTO `question_submit` VALUES (6, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:03:27', '2023-12-04 19:03:27', 0);
INSERT INTO `question_submit` VALUES (7, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:05:39', '2023-12-04 19:05:39', 0);
INSERT INTO `question_submit` VALUES (8, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:06:58', '2023-12-04 19:06:58', 0);
INSERT INTO `question_submit` VALUES (9, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:14:25', '2023-12-04 19:14:25', 0);
INSERT INTO `question_submit` VALUES (10, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:27:36', '2023-12-04 19:27:36', 0);
INSERT INTO `question_submit` VALUES (11, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:32:57', '2023-12-04 19:32:58', 0);
INSERT INTO `question_submit` VALUES (12, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:35:55', '2023-12-04 19:35:55', 0);
INSERT INTO `question_submit` VALUES (13, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:38:06', '2023-12-04 19:38:06', 0);
INSERT INTO `question_submit` VALUES (14, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-04 19:50:45', '2023-12-04 19:50:45', 0);
INSERT INTO `question_submit` VALUES (15, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":0}', 2, 2, 1, '2023-12-04 19:56:39', '2023-12-04 19:56:51', 0);
INSERT INTO `question_submit` VALUES (16, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":22}', 2, 2, 1, '2023-12-04 19:59:58', '2023-12-04 20:00:05', 0);
INSERT INTO `question_submit` VALUES (17, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":21}', 2, 2, 1, '2023-12-04 20:06:38', '2023-12-04 20:06:38', 0);
INSERT INTO `question_submit` VALUES (18, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":20}', 2, 2, 1, '2023-12-04 20:08:56', '2023-12-04 20:31:01', 0);
INSERT INTO `question_submit` VALUES (19, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 15:36:30', '2023-12-05 15:36:30', 0);
INSERT INTO `question_submit` VALUES (20, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:13:46', '2023-12-05 16:13:46', 0);
INSERT INTO `question_submit` VALUES (21, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:16:54', '2023-12-05 16:16:54', 0);
INSERT INTO `question_submit` VALUES (22, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":17568}', 2, 2, 1, '2023-12-05 16:27:09', '2023-12-05 16:27:52', 0);
INSERT INTO `question_submit` VALUES (23, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":152037}', 2, 2, 1, '2023-12-05 16:28:41', '2023-12-05 16:31:29', 0);
INSERT INTO `question_submit` VALUES (24, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"WrongAnswer\",\"time\":20797}', 2, 2, 1, '2023-12-05 16:37:13', '2023-12-05 16:37:50', 0);
INSERT INTO `question_submit` VALUES (25, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:39:11', '2023-12-05 16:39:11', 0);
INSERT INTO `question_submit` VALUES (26, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:43:24', '2023-12-05 16:43:24', 0);
INSERT INTO `question_submit` VALUES (27, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:47:47', '2023-12-05 16:47:47', 0);
INSERT INTO `question_submit` VALUES (28, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:48:51', '2023-12-05 16:48:51', 0);
INSERT INTO `question_submit` VALUES (29, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:49:46', '2023-12-05 16:49:46', 0);
INSERT INTO `question_submit` VALUES (30, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:53:55', '2023-12-05 16:53:55', 0);
INSERT INTO `question_submit` VALUES (31, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:55:08', '2023-12-05 16:55:08', 0);
INSERT INTO `question_submit` VALUES (32, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 16:56:43', '2023-12-05 16:56:43', 0);
INSERT INTO `question_submit` VALUES (33, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-05 17:11:44', '2023-12-05 17:11:44', 0);
INSERT INTO `question_submit` VALUES (34, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":1047}', 2, 2, 1, '2023-12-05 17:14:26', '2023-12-05 17:14:35', 0);
INSERT INTO `question_submit` VALUES (35, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"TimeLimitExceeded\",\"time\":1819}', 2, 2, 1, '2023-12-05 17:15:28', '2023-12-05 17:15:36', 0);
INSERT INTO `question_submit` VALUES (36, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":35}', 2, 2, 1, '2023-12-05 17:16:51', '2023-12-05 17:16:52', 0);
INSERT INTO `question_submit` VALUES (37, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":33}', 2, 2, 1, '2023-12-05 17:56:51', '2023-12-05 17:56:53', 0);
INSERT INTO `question_submit` VALUES (38, 'java', 'import java.util.Scanner;\n\nclass Main {\n    public static void main(String[] args) {\n        Scanner scan = new Scanner(System.in);\n        int a = scan.nextInt();\n        int b = scan.nextInt();\n        System.out.println(a+b);\n    }\n}', '{\"message\":\"WrongAnswer\",\"memory\":0,\"time\":102}', 2, 2, 1, '2023-12-05 17:57:08', '2023-12-05 17:57:09', 0);
INSERT INTO `question_submit` VALUES (39, 'java', 'import java.util.Scanner;\n\nclass Main {\n    public static void main(String[] args) {\n        Scanner scan = new Scanner(System.in);\n        int a = scan.nextInt();\n        int b = scan.nextInt();\n        System.out.println(a+b);\n    }\n}', '{\"message\":\"WrongAnswer\",\"memory\":0,\"time\":0}', 2, 2, 1, '2023-12-05 17:59:54', '2023-12-05 18:00:49', 0);
INSERT INTO `question_submit` VALUES (40, 'java', 'import java.util.Scanner;\n\nclass Main {\n    public static void main(String[] args) {\n        Scanner scan = new Scanner(System.in);\n        int a = scan.nextInt();\n        int b = scan.nextInt();\n        System.out.println(a+b);\n    }\n}', '{\"message\":\"Accepted\",\"memory\":0,\"time\":103}', 2, 2, 1, '2023-12-05 18:01:01', '2023-12-05 18:01:05', 0);
INSERT INTO `question_submit` VALUES (41, 'java', 'import java.util.Scanner;\n\nclass Main {\n    public static void main(String[] args) {\n        Scanner scan = new Scanner(System.in);\n        int a = scan.nextInt();\n        int b = scan.nextInt();\n        System.out.println(a+b);\n    }\n}', '{\"message\":\"Accepted\",\"memory\":0,\"time\":106}', 2, 2, 1, '2023-12-05 18:05:29', '2023-12-05 18:05:30', 0);
INSERT INTO `question_submit` VALUES (42, 'cpp', '#include<iostream>\nusing namespace std;\n\nint main()\n{\n    int a,b; cin>>a>>b;\n    cout<<a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":35}', 2, 2, 1, '2023-12-05 18:05:44', '2023-12-05 18:05:44', 0);
INSERT INTO `question_submit` VALUES (43, 'python', 'a = int(input())\nb = int(input())\nprint(a + b)', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-06 16:59:49', '2023-12-06 16:59:49', 0);
INSERT INTO `question_submit` VALUES (44, 'python', 'a = int(input())\nb = int(input())\nprint(a + b)', '{\"message\":\"WrongAnswer\",\"time\":0}', 2, 2, 1, '2023-12-06 17:05:46', '2023-12-06 17:05:55', 0);
INSERT INTO `question_submit` VALUES (45, 'python', 'a,b=map(int,input().split())\nprint(a + b)', '{\"message\":\"Accepted\",\"time\":63}', 2, 2, 1, '2023-12-06 17:20:52', '2023-12-06 17:20:53', 0);
INSERT INTO `question_submit` VALUES (46, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 2, '2023-12-07 21:24:39', '2023-12-07 21:24:39', 0);
INSERT INTO `question_submit` VALUES (47, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cout<< a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":38}', 2, 2, 2, '2023-12-07 21:25:21', '2023-12-07 21:25:22', 0);
INSERT INTO `question_submit` VALUES (48, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cout<< a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":29}', 2, 2, 2, '2023-12-08 15:04:29', '2023-12-08 15:04:38', 0);
INSERT INTO `question_submit` VALUES (49, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 2, '2023-12-08 15:07:00', '2023-12-08 15:07:00', 0);
INSERT INTO `question_submit` VALUES (50, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 3, '2023-12-08 15:14:04', '2023-12-08 15:14:04', 0);
INSERT INTO `question_submit` VALUES (51, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cout<< a+b;\n    return 0;\n}', '{\"message\":\"Accepted\",\"time\":30}', 2, 2, 3, '2023-12-08 16:07:29', '2023-12-08 16:07:31', 0);
INSERT INTO `question_submit` VALUES (52, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 3, '2023-12-08 16:11:49', '2023-12-08 16:11:49', 0);
INSERT INTO `question_submit` VALUES (53, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 3, '2023-12-08 16:17:24', '2023-12-08 16:17:24', 0);
INSERT INTO `question_submit` VALUES (54, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"Waiting\"}', 1, 2, 3, '2023-12-08 16:17:46', '2023-12-08 16:17:46', 0);
INSERT INTO `question_submit` VALUES (55, 'cpp', '#include<iostream>\n\nusing namespace std;\n\nint main()\n{\n    int a,b;\n    cin>>a >> b;\n    cont<< a+b;\n    return 0;\n}', '{\"message\":\"CompileError\",\"memory\":0,\"time\":0}', 2, 2, 3, '2023-12-08 16:26:32', '2023-12-08 16:26:42', 0);
INSERT INTO `question_submit` VALUES (56, 'python', 'a,b=map(int,input().split())\nprint(a + b)', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-08 17:38:43', '2023-12-08 17:38:43', 0);
INSERT INTO `question_submit` VALUES (57, 'python', 'a,b=map(int,input().split())\nprint(a + b)', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-08 17:39:53', '2023-12-08 17:39:53', 0);
INSERT INTO `question_submit` VALUES (58, 'python', 'a,b=map(int,input().split())\nprint(a + b)', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-08 17:43:04', '2023-12-08 17:43:04', 0);
INSERT INTO `question_submit` VALUES (59, 'python', 'a,b=map(int,input().split())\nprint(a + b)', '{\"message\":\"Waiting\"}', 1, 2, 1, '2023-12-08 17:46:33', '2023-12-08 17:46:33', 0);
INSERT INTO `question_submit` VALUES (60, 'python', 'a,b=map(int,input().split())\nprint(a + b)', '{\"message\":\"Accepted\",\"time\":51}', 2, 2, 1, '2023-12-08 17:49:42', '2023-12-08 17:49:42', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'chanchan', '9bc6999639c8b7a94af785aabb8d83dd', 'chanchan', NULL, NULL, 'admin', '2023-11-20 18:22:11', '2023-11-20 18:23:26', 0);
INSERT INTO `user` VALUES (2, 'admin', '9bc6999639c8b7a94af785aabb8d83dd', 'admin', NULL, NULL, 'admin', '2023-12-06 17:38:38', '2023-12-06 18:37:30', 0);
INSERT INTO `user` VALUES (3, 'test', '9bc6999639c8b7a94af785aabb8d83dd', 'test', NULL, NULL, 'user', '2023-12-06 18:37:42', '2023-12-06 18:41:11', 0);

SET FOREIGN_KEY_CHECKS = 1;
