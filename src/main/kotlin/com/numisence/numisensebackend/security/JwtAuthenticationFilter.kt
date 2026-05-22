package com.numisence.numisensebackend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // 1. Check if the header contains a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // 2. Extract token and email
        val jwt = authHeader.substring(7)
        val userEmail = try {
            jwtService.extractEmail(jwt)
        } catch (e: Exception) {
            null
        }

        // 3. If email is valid and user is not already authenticated in this context
        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            if (jwtService.isTokenValid(jwt)) {
                // 4. Create the Authentication object
                val authToken = UsernamePasswordAuthenticationToken(
                    userEmail,
                    null,
                    emptyList() // Roles/Authorities can be mapped here if needed
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                // 5. Inject the Authentication into Spring's Security Context
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}