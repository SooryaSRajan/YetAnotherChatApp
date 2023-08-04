import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

type LoginRequest = {
    username: string;
    password: string;
};

export type LoginResponse = {
    data: {
        token?: string;
        displayName: string;
    }
    error?: string;
}


export const apiSlice = createApi({
    reducerPath: 'api',
    baseQuery: fetchBaseQuery(),
    endpoints: (builder) => ({
        login: builder.mutation<LoginResponse, LoginRequest>({
            query: (credentials) => ({
                url: '/api/authentication/signin',
                method: 'POST',
                body: credentials,
            }),
        }),
    }),
});

export const { useLoginMutation } = apiSlice;
export const { endpoints } = apiSlice;
export const { login } = endpoints;