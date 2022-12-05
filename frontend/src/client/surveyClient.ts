import axios from "axios";
import { config } from "../config";
import { Question, Program } from "../types";

const makeSurveyClient = () => {
  const httpClient = axios.create({
    baseURL: config.serverUrl,
  });

  // Fail if API doesn't return 2xx
  httpClient.interceptors.response.use(undefined, (error) => {
    window.alert("Something has gone terribly wrong :(\n\n" + error);

    throw error;
  });

  const getTotalProgramsCount = async () => {
    const { data: servicesCount } = await httpClient.get<number>("programs/getTotalProgramsCount");

    return servicesCount;
  };

  const startNewSession = async () => {
    const { data: sessionId } = await httpClient.post<string>("sessions/createSession");

    return sessionId;
  };

  const getFirstQuestion = async () => {
    const queryParams = new URLSearchParams({
      sessionId: "",
      surveyId: "-1",
      lastQuestionId: "-1",
      lastAnswerIds: "-1",
      lastAnswerInput: "",
    });

    const { data: firstQuestion } = await httpClient.get<Question>(`questions/getNextQuestion?${queryParams}`);

    return firstQuestion;
  };

  const getSurveyIdFromAge = async (sessionId: string, age: string) => {
    const queryParams = new URLSearchParams({
      sessionId,
      age,
    });

    const { data: surveyId } = await httpClient.get<number>(`sessions/getSurveyIdFromAge?${queryParams}`);

    return surveyId;
  };

  const getSecondQuestion = async (sessionId: string, questionNumber: number, age: string) => {
    const queryParams = new URLSearchParams({
      sessionId: sessionId,
      surveyId: "-1",
      lastQuestionId: questionNumber.toString(),
      lastAnswerIds: age,
      lastAnswerInput: "",
    });

    const { data: secondQuestion } = await httpClient.get<Question>(`questions/getNextQuestion?${queryParams}`);

    return secondQuestion;
  };

  // TODO: Fix the params of this const
  const getNextQuestion = async (
    sessionId: string,
    questionNumber: number,
    surveyId: number,
    lastAnswerIds: string,
    lastAnswerInput: string
  ) => {
    const queryParams = new URLSearchParams({
      sessionId: sessionId,
      surveyId: surveyId.toString(),
      lastQuestionId: questionNumber.toString(),
      lastAnswerIds: lastAnswerIds,
      lastAnswerInput,
    });

    const { data: nextQuestion } = await httpClient.get<Question>(`questions/getNextQuestion?${queryParams}`);

    return nextQuestion;
  };

  const createUser = async (name: string, email: string, phone: string) => {
    const queryParams = new URLSearchParams({
      name: name,
      email: email,
      phone: phone,
    });

    const { data: userid } = await httpClient.get<string>(`users/createUser?${queryParams}`);

    return userid;
  };

  const addUserId = async (sessionId: string, userId: string) => {
    const queryParams = new URLSearchParams({
      sessionId: sessionId,
      userId: userId,
    });

    const { data: sid } = await httpClient.get<string>(`sessions/addUserId?${queryParams}`);

    return sid;
  };

  const getEligiblePrograms = async (sessionId: string) => {
    const queryParams = new URLSearchParams({
      sessionId: sessionId,
    });

    const { data: eligiblePrograms } = await httpClient.get<Program[]>(`sessions/getEligiblePrograms?${queryParams}`);

    return eligiblePrograms;
  };

  const getAllPrograms = async (sessionId: string) => {
    const queryParams = new URLSearchParams({
      sessionId: sessionId,
    });

    const { data: allPrograms } = await httpClient.get<Program[]>(`sessions/getAllProgramsBySessionId?${queryParams}`);

    return allPrograms;
  };

  return {
    getFirstQuestion,
    getTotalProgramsCount,
    startNewSession,
    getSecondQuestion,
    getNextQuestion,
    getSurveyIdFromAge,
    createUser,
    addUserId,
    getEligiblePrograms,
    getAllPrograms,
  };
};

export const surveyClient = makeSurveyClient();
