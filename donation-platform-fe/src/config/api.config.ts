export const API_CONFIG = {
  BASE_URL: 'http://localhost:8080/api',
  ENDPOINTS: {
    AUTH: {
      LOGIN: '/users/login',
      SIGNUP: '/users/signup',
      FORGOT_PASSWORD: '/users/forgot-password'
    },
    CAMPAIGNS: {
      BASE: '/campaigns',
      ACTIVE: '/campaigns/active',
      BY_ID: (id: number) => `/campaigns/${id}`
    },
    VOLUNTEER: {
      POSITIONS: '/volunteer/positions',
      APPLY: '/volunteer/apply',
      REGISTER: '/volunteers/register',
      COUNT: (eventId: number) => `/volunteers/count/${eventId}`,
      USER_SHIFTS: '/volunteers/user/shifts'
    },
    PROGRAMS: {
      BASE: '/programs',
      BY_ID: (id: number) => `/programs/${id}`
    }
  }
} as const; 