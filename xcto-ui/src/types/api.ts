/** 后端返回的题目结构 */
export interface Question {
  id: number
  classId: number
  questionType: number
  question: string
  options: Record<string, string>
}

/** 前端使用的选项格式 */
export interface QuestionOption {
  value: string
  text: string
}

/** 班级信息 */
export interface Course {
  id: number
  classId: number
  className: string
  questionTotal: number
}

/** 答案检查结果 */
export interface CheckResult {
  correct: boolean
  correctAnswers: string[]
}

/** 登录接口返回 */
export interface LoginResult {
  tokenName: string
  tokenValue: string
  userId: number
  username: string
  uploadBalance: number
}

/** 用户信息 */
export interface UserInfo {
  id: number
  username: string
  role: string
  courses: string
  uploadBalance: number
  uploadUsed: number
  status: number
}
