package ro.traistaruandszasz.rssfeed.socket.handle;

public enum SocketMessageType {
    Logging, Register, Comment, Friendship, AddingNews, QueryLoadingNews, CopyNewsToAnotherUser,QueryLoadingComment,QueryLoadingCategory
    ,QueryLoadingAllUsers,QueryFriendshipExists,QueryAcceptFriendRequest,QueryExistsFriendRequest,QueryInsertNews,QueryInsertComment
    ,QuerySendFriendRequest,QuerySendMessage,QueryLoadingMessages,QueryInsertCategory,QueryInsertKeyword,QueryLoadKeywords,
    QueryUpdateCommentList, QueryFriendshipRestriction, QueryFriendshipSetRestriction
}

